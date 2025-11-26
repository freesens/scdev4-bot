package kr.co.pionnet.scdev4.bot.domain.review.service;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.pionnet.scdev4.bot.domain.common.util.TelegramUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;

@Slf4j
@Service
public class GithubWebhookService {
    @Value("${telegram.bot-token.review}")
    private String BOT_TOKEN_REVIEW;

    @Value("${telegram.chat-id.code-review}")
    private String CHAT_ID_REVIEW;

    @Value("${telegram.chat-id.dev4-employee}")
    private String CHAT_ID_NOTICE;

    @Value("${code-review.target.branch}")
    private String REVIEW_TARGET_BRANCH;

    private final TelegramUtil telegramUtil;

    @Autowired
    public GithubWebhookService(TelegramUtil telegramUtil) {
        this.telegramUtil = telegramUtil;
    }

    public String parseToMessage(HttpServletRequest request) {
        StringBuffer result = new StringBuffer();
        JSONObject resultJson = new JSONObject();
        boolean isBreak = true;

        try {
            String botToken = BOT_TOKEN_REVIEW;
            String chatId = CHAT_ID_NOTICE;

            JSONObject githubJson = new JSONObject(receiveHookMessage(request));
            if (log.isDebugEnabled()) {
                log.debug("===================================================================");
                log.debug(githubJson.toString());
                log.debug("===================================================================");
            }
            result.setLength(0);


            String repositoryName = githubJson.getJSONObject("repository").getString("full_name");
            JSONObject pullRequest = githubJson.getJSONObject("pull_request");

            if (pullRequest.has("base")) {
                if(pullRequest.getJSONObject("base").has("ref")) {
                    isBreak = !REVIEW_TARGET_BRANCH.equals(pullRequest.getJSONObject("base").getString("ref"));
                }
            }
            if (isBreak) {
                result.append("invallid P/R target.");
                result.append("\r\nsource : ").append(pullRequest.getJSONObject("head").getString("ref"));
                result.append("\r\ntarget : ").append(pullRequest.getJSONObject("base").getString("ref"));
                return result.toString();
            }

            String action = githubJson.getString("action");
            String state;

            switch(action) {
                case "created":
                case "edited":
                    if (actionForCommentCreated(result, githubJson, pullRequest)) {
                        return result.toString();
                    } else {
                        chatId = CHAT_ID_REVIEW;
                    }

                    break;
                case "opened":
                    actionForPullRequestOpened(result, repositoryName, pullRequest);

                    break;
                case "synchronize":
                    actionForPullRequestSynchronize(result, repositoryName, pullRequest);

                    break;
                case "submitted":
                    if (actionForSubmmited(result, githubJson, repositoryName, pullRequest)) {
                        return result.toString();
                    } else {
                        chatId = CHAT_ID_REVIEW;
                    }

                    break;
                case "closed":
                    actionForClosed(result, repositoryName, pullRequest);

                    break;
                case "resolved":
                    return result.toString();
                default:
                    chatId = CHAT_ID_REVIEW;    // TODO etc chat_id
                    result.append(getPullRequestTitle(pullRequest));
                    result.append("\r\nUnknown action type : ").append(action);
                    //result.append("\r\n\r\n").append(getPullRequestUrl(repositoryName, pullRequest));
            }


            if (log.isDebugEnabled()) {
                log.debug(result.toString());
            }

            telegramUtil.sendMessage(result.toString(), botToken, chatId);
            resultJson.put("result", "Success");
        } catch (Exception e) {
            resultJson.put("result", "Fail");
            resultJson.put("failMessage", e.getMessage());
            log.error(e.getLocalizedMessage(), e);
        } finally {
            result.setLength(0);
            result.append(resultJson.toString());

            return result.toString();
        }

    }

    private String receiveHookMessage(HttpServletRequest request) {
        StringBuffer result = new StringBuffer();
        String line;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
        } finally {
            return result.toString();
        }
    }

    private boolean actionForCommentCreated(StringBuffer result, JSONObject githubJson, JSONObject pullRequest) {
        result.append(getPullRequestTitle(pullRequest));
        if (!githubJson.has("comment")) {
            return true;
        }

        JSONObject comment = githubJson.getJSONObject("comment");

        result.append("\r\nReviewer : ").append(getLoginName(comment.getJSONObject("user")));
        result.append("\r\n\r\n").append(comment.getString("body"));

        return false;
    }

    private void actionForPullRequestOpened(StringBuffer result, String repositoryName, JSONObject pullRequest) {
        result.append(getPullRequestTitle(pullRequest));
        result.append("\r\nRequestor : ").append(getLoginName(pullRequest.getJSONObject("user")));
        result.append("\r\n\r\nP/R건 리뷰 부탁 드립니다.");
        result.append("\r\n\r\n").append(getPullRequestUrl(repositoryName, pullRequest));
    }

    private void actionForPullRequestSynchronize(StringBuffer result, String repositoryName, JSONObject pullRequest) {
        result.append(getPullRequestTitle(pullRequest));
        result.append("\r\nRequestor : ").append(getLoginName(pullRequest.getJSONObject("user")));
        result.append("\r\n\r\nP/R건 리뷰 내역 수정 후 반영 했으니, 다시 리뷰 부탁 드립니다.");
        result.append("\r\n\r\n").append(getPullRequestUrl(repositoryName, pullRequest));
    }

    private boolean actionForSubmmited(StringBuffer result, JSONObject githubJson, String repositoryName, JSONObject pullRequest) {
        String state;
        /*
         * review.state = commented / approved
         */

        if (!githubJson.has("review")) {
            return true;
        }

        JSONObject review = githubJson.getJSONObject("review");

        state = review.getString("state");
        result.append(getPullRequestTitle(pullRequest));
        switch (state) {
            case "commented":
                result.append("\r\nCommented by ");
                return true;
            //break;
            case "approved":
                result.append("\r\nApproved by ");
                break;
            default:
                result.append("\r\nstate : ").append(state).append("\r\nlogin by ");
        }
        result.append(getLoginName(review.getJSONObject("user")));
        //result.append("\r\n\r\n").append(getPullRequestUrl(repositoryName, pullRequest));
        return false;
    }

    private void actionForClosed(StringBuffer result, String repositoryName, JSONObject pullRequest) {
        /**
         * pull_request.merged_by.login
         * pull_request.title
         * pull_request.url
         * pull_request.state
         */
        result.append("머지 완료");
        result.append("\r\n\r\n").append(getPullRequestTitle(pullRequest));
        result.append("\r\nMerged by ").append(getLoginName(pullRequest.getJSONObject("merged_by")));
        //result.append("\r\n\r\n").append(getPullRequestUrl(repositoryName, pullRequest));
    }

    private String getPullRequestTitle(JSONObject pullRequest) {
        return pullRequest.getString("title");
    }

    private String getLoginName(JSONObject json) {
        if (json.has("login")) {
            return json.getString("login");
        }

        return "";
    }

    private String getPullRequestUrl(String repositoryName, JSONObject json) {
        if (json.has("number")) {
            return "https://github.com/" + repositoryName + "/pull/" + json.getInt("number");
        }

        return "";
    }
}
