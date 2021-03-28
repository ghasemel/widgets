package com.elyasi.assignments.widgets.web.controller;

import com.elyasi.assignments.widgets.constant.GlobalConstant;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({GlobalConstant.PROFILE_IN_MEMORY, GlobalConstant.PROFILE_DEV})
class WidgetControllerInMemoryITTest extends WidgetControllerITest {

}