package com.bahamaeatsdriver.model_class.job_history

data class Body(
    val pastJobHistory: List<PastJobHistory>,
    val upComingJobHistory: UpComingJobHistory
)