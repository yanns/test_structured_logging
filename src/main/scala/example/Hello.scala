package example

import com.typesafe.scalalogging.StrictLogging


object Hello extends StrictLogging {

  def main(args: Array[String]): Unit = {
    logger.error("hello error")
    logger.warn("hello warn")
    logger.info("hello info")
    logger.debug("hello debug")
    println("done")
  }

}

