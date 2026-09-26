package id.codemockup.template.core.network


import java.io.IOException

class ApiException(val statusCode: Int, message: String) : IOException(message)
