package pl.weblang.background

import pl.weblang.background.source.JobResult

interface JobBuilder {
    fun createJob(segment: Segment, timeStamp: Long = System.currentTimeMillis()): () -> JobResult
}
