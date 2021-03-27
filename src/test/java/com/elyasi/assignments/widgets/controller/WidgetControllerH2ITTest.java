package com.elyasi.assignments.widgets.controller;

import com.elyasi.assignments.widgets.TestHelper;
import com.elyasi.assignments.widgets.constant.GlobalConstant;
import com.elyasi.assignments.widgets.dto.WidgetDto;
import com.elyasi.assignments.widgets.exception.defined.WidgetNotFoundException;
import com.elyasi.assignments.widgets.exception.defined.bad.MutabilityException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static com.elyasi.assignments.widgets.TestHelper.asJsonString;
import static com.elyasi.assignments.widgets.TestHelper.asWidgetObj;
import static com.elyasi.assignments.widgets.constant.ControllerConstant.WIDGET_ENDPOINT;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({GlobalConstant.PROFILE_H2, GlobalConstant.PROFILE_DEV})
class WidgetControllerH2ITTest extends WidgetControllerITest {

}