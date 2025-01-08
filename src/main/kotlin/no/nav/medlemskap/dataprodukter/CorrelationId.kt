package no.nav.medlemskap.dataprodukter


import org.slf4j.MDC
import java.util.*


val callIdGenerator: ThreadLocal<String> = ThreadLocal.withInitial {
    UUID.randomUUID().toString()
}

class CorrelationId(private val id: String) {
    override fun toString(): String = id
}

internal fun getCorrelationId(): CorrelationId {
    if (MDC.get(MDC_CALL_ID) == null) {
        return CorrelationId(callIdGenerator.get())
    }
    return CorrelationId(MDC.get(MDC_CALL_ID))
}
internal fun getCorrelationId(callId: String?): CorrelationId {
    if (callId == null) {
        return CorrelationId(callIdGenerator.get())
    }
    return CorrelationId(callId)
}
