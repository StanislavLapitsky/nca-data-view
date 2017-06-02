package org.nca.data

/**
 * @author stanislav.lapitsky created 5/30/2017.
 */
//import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.support.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

@SpringBootApplication
//@EnableAutoConfiguration(exclude = arrayOf(DataSourceAutoConfiguration::class))
@EnableAutoConfiguration
open class Application : SpringBootServletInitializer() {

    @Bean
    open fun mapperForKotlinTypes(): MappingJackson2HttpMessageConverter {
//        return MappingJackson2HttpMessageConverter().apply { objectMapper = jacksonMapper }
        return MappingJackson2HttpMessageConverter()
    }

    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder =
            application.sources(Application::class.java)

    companion object {

//        val jacksonMapper = ObjectMapper().registerKotlinModule()
//                .setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
//                .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            println("starting application...")
            SpringApplication.run(Application::class.java, *args)
        }
    }

}