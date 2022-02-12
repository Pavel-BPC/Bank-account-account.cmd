package com.techbank.account.cmd.api.controllers;

import com.techbank.account.cmd.api.commands.RestoreReadDbCommand;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/restoreReadDb")
public class RestoreReadDbController {
    @Autowired
    private CommandDispatcher commandDispatcher;

    private final Logger logger = Logger.getLogger(RestoreReadDbController.class.getName());

    @PostMapping
    public ResponseEntity<BaseResponse> restoreReadDb() {
        try {
            commandDispatcher.send(new RestoreReadDbCommand());
            return new ResponseEntity<>(new BaseResponse("Restore read DB request completed successfully"), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            var safeErrorMessages = "Error while processing request to restore read DB";
            logger.log(Level.SEVERE, safeErrorMessages, e);
            return new ResponseEntity<>(new BaseResponse(safeErrorMessages), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
