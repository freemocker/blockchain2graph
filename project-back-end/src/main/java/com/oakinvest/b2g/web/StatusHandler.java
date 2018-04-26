package com.oakinvest.b2g.web;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Controller for the status.
 * Created by straumat on 31/10/16.
 */
@Component
public class StatusHandler extends TextWebSocketHandler {

    /**
     * Param for message type.
     */
    private static final String PARAM_MESSAGE_TYPE = "messageType";

    /**
     * Param for message value.
     */
    private static final String PARAM_MESSAGE_VALUE = "messageValue";

    /**
     * Blocks in bitcoin core.
     */
    private static final String TYPE_BLOCKS_IN_BITCOIN_CORE = "blocksInBitcoinCore";

    /**
     * Blocks in neo4j.
     */
    private static final String TYPE_BLOCKS_IN_NEO4J = "blocksInNeo4j";

    /**
     * Block import duration.
     */
    private static final String TYPE_BLOCK_IMPORT_DURATION = "blockImportDuration";

    /**
     * Log type.
     */
    private static final String TYPE_LOG = "log";

    /**
     * Error message.
     */
    private static final String TYPE_ERROR = "error";

    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(StatusHandler.class);

    /**
     * Sessions.
     */
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    /**
     * Gson.
     */
    private final Gson gson = new Gson();

    /**
     * Last log message.
     */
    private String lastLogMessage = "";

    /**
     * Last error message.
     */
    private String lastErrorMessage = "";

    /**
     * Total block count.
     */
    private int lastTotalBlockCount = -1;

    /**
     * Imported block count.
     */
    private int lastImportedBlockCount = -1;

    /**
     * Last average block import duration.
     */
    private float lastAverageBlockImportDuration = -1;

    @Override
    public final void afterConnectionEstablished(final WebSocketSession newSession) {
        this.sessions.add(newSession);
        updateBlocksInNeo4j(lastImportedBlockCount);
        updateBlocksInBitcoinCore(lastTotalBlockCount);
        if (!"".equals(lastErrorMessage)) {
            updateError(lastErrorMessage);
        }
        updateLog(lastLogMessage);
        if (lastAverageBlockImportDuration != -1) {
            updateAverageBlockImportDuration(lastAverageBlockImportDuration);
        }
    }

    /**
     * Blocks in neo4j.
     *
     * @param count new value.
     */
    public final void updateBlocksInNeo4j(final int count) {
        lastImportedBlockCount = count;
        HashMap<Object, Object> information = new HashMap<>();
        information.put(PARAM_MESSAGE_TYPE, TYPE_BLOCKS_IN_NEO4J);
        information.put(PARAM_MESSAGE_VALUE, count);
        sendMessage(gson.toJson(information));
    }

    /**
     * Blocks in bitcoin core.
     *
     * @param count new value.
     */
    public final void updateBlocksInBitcoinCore(final int count) {
        lastTotalBlockCount = count;
        HashMap<Object, Object> information = new HashMap<>();
        information.put(PARAM_MESSAGE_TYPE, TYPE_BLOCKS_IN_BITCOIN_CORE);
        information.put(PARAM_MESSAGE_VALUE, count);
        sendMessage(gson.toJson(information));
    }

    /**
     * Update the log.
     *
     * @param logMessage log message
     */
    public final void updateLog(final String logMessage) {
        lastLogMessage = logMessage;
        HashMap<Object, Object> information = new HashMap<>();
        information.put(PARAM_MESSAGE_TYPE, TYPE_LOG);
        information.put(PARAM_MESSAGE_VALUE, logMessage);
        //sendMessage(gson.toJson(information));
    }

    /**
     * Update error message.
     *
     * @param errorMessage error message.
     */
    public final void updateError(final String errorMessage) {
        lastErrorMessage = errorMessage;
        HashMap<Object, Object> information = new HashMap<>();
        information.put(PARAM_MESSAGE_TYPE, TYPE_ERROR);
        information.put(PARAM_MESSAGE_VALUE, errorMessage);
        sendMessage(gson.toJson(information));
    }

    /**
     * Update execution time statistic.
     *
     * @param averageBlockImportDuration new execution time statistics.
     */
    public final void updateAverageBlockImportDuration(final float averageBlockImportDuration) {
        lastAverageBlockImportDuration = averageBlockImportDuration;
        HashMap<Object, Object> information = new HashMap<>();
        information.put(PARAM_MESSAGE_TYPE, TYPE_BLOCK_IMPORT_DURATION);
        information.put(PARAM_MESSAGE_VALUE, averageBlockImportDuration);
        sendMessage(gson.toJson(information));
    }

    /**
     * Send a message message to all sessions.
     *
     * @param message message
     */
    private void sendMessage(final String message) {
        try {
            // We send the messages to all opened sessions. We delete the one that are closed
            for (WebSocketSession session : this.sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                } else {
                    sessions.remove(session);
                }
            }
        } catch (Exception e) {
            log.warn("Error sending message : " + e.getMessage());
        }
    }

    @Override
    protected final void handleTextMessage(final WebSocketSession session, final TextMessage message) {
        try {
            if (session.isOpen()) {
                session.sendMessage(message);
            } else {
                sessions.remove(session);
            }
        } catch (Exception e) {
            log.warn("Error sending message : " + e.getMessage());
        }
    }

}
