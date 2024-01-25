import com.fasterxml.jackson.core.type.TypeReference

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

class JsonHandler(val path: String) {
    val mapper = ObjectMapper()

    fun <T> save(data: T) {
        mapper.writeValue(File(path), data)
    }

    fun <T> load(): T {
        return mapper.readValue(File(path), object : TypeReference<T>() {})
    }
}