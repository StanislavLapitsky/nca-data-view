package org.nca.data.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * @author stanislav.lapitsky created 6/1/2017.
 */
@Controller
class HomeController {
    @RequestMapping(path = arrayOf("/"), method = arrayOf(RequestMethod.GET))
    fun getHome(): String {
        return "home"
    }
}