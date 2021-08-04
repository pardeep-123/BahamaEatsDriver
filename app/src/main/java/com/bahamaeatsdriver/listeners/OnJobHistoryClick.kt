package com.bahamaeatsdriver.listeners

import com.bahamaeatsdriver.model_class.job_history.PastJobHistory
import com.bahamaeatsdriver.model_class.job_history.UpComingJobHistory

interface OnJobHistoryClick {
    fun onPastJobHistoryClick(position: Int, data: PastJobHistory)
    fun onUpComingJobHistoryClick(position: Int, data: UpComingJobHistory)
}