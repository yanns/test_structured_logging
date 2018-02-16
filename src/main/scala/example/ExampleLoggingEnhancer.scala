package example

import com.google.cloud.logging.{LogEntry, LoggingEnhancer}

class ExampleLoggingEnhancer extends LoggingEnhancer {
  override def enhanceLogEntry(builder: LogEntry.Builder): Unit = {
    builder.addLabel("my-key", "my value")
  }
}
