package com.agenda.app.di

import com.agenda.app.data.parser.DateTimeExtractorImpl
import com.agenda.app.data.parser.TextParserImpl
import com.agenda.app.data.service.AlarmSchedulerImpl
import com.agenda.app.data.service.AudioRecorderImpl
import com.agenda.app.domain.parser.DateTimeExtractor
import com.agenda.app.domain.parser.TextParserContract
import com.agenda.app.domain.service.AlarmSchedulerContract
import com.agenda.app.domain.service.AudioRecorderContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    
    @Binds
    abstract fun bindAudioRecorder(
        audioRecorderImpl: AudioRecorderImpl
    ): AudioRecorderContract
    
    @Binds
    abstract fun bindTextParser(
        textParserImpl: TextParserImpl
    ): TextParserContract
    
    @Binds
    abstract fun bindDateTimeExtractor(
        dateTimeExtractorImpl: DateTimeExtractorImpl
    ): DateTimeExtractor
    
    @Binds
    abstract fun bindAlarmScheduler(
        alarmSchedulerImpl: AlarmSchedulerImpl
    ): AlarmSchedulerContract
}
