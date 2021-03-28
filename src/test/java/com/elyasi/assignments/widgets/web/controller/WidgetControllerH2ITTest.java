package com.elyasi.assignments.widgets.web.controller;

import com.elyasi.assignments.widgets.constant.GlobalConstant;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({GlobalConstant.PROFILE_H2, GlobalConstant.PROFILE_DEV})
class WidgetControllerH2ITTest extends WidgetControllerITest {

}