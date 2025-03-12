package com.log.aop_lib.logging;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class LoggingAspectTest {

    @InjectMocks
    private LoggingAspect loggingAspect;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private MethodSignature methodSignature;

    @Mock
    private Appender<ILoggingEvent> mockAppender;

    @Captor
    private ArgumentCaptor<ILoggingEvent> logCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Logger logger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);
        logger.addAppender(mockAppender);

        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getDeclaringTypeName()).thenReturn("com.log.aop_lib.test.TestController");
        when(methodSignature.getName()).thenReturn("testMethod");
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{"arg1", "arg2"});
    }

    @Test
    void testLogControllerExecution_Success() throws Throwable {
        when(proceedingJoinPoint.proceed()).thenReturn("SuccessResponse");

        Object result = loggingAspect.logControllerExecution(proceedingJoinPoint);

        verify(mockAppender, atLeastOnce()).doAppend(logCaptor.capture());

        boolean foundEntryLog = logCaptor.getAllValues().stream()
                .anyMatch(event -> event.getFormattedMessage().contains("Entrando no método"));

        boolean foundExitLog = logCaptor.getAllValues().stream()
                .anyMatch(event -> event.getFormattedMessage().contains("Saída do método"));

        assertTrue(foundEntryLog, "O log de entrada não foi capturado");
        assertTrue(foundExitLog, "O log de saída não foi capturado");
        assert result.equals("SuccessResponse");
    }

    @Test
    void testLogControllerExecution_Exception() throws Throwable {
        RuntimeException exception = new RuntimeException("Erro simulado");
        when(proceedingJoinPoint.proceed()).thenThrow(exception);

        try {
            loggingAspect.logControllerExecution(proceedingJoinPoint);
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
        }

        verify(mockAppender, atLeastOnce()).doAppend(logCaptor.capture());

        boolean foundErrorLog = logCaptor.getAllValues().stream()
                .anyMatch(event -> event.getFormattedMessage().contains("Exceção no método"));

        assertTrue(foundErrorLog, "O log de erro não foi capturado");
    }
}
