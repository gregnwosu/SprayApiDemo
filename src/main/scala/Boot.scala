package com.christophergagne.sprayapidemo

import akka.actor.{ActorSystem, Props}
import akka.event.Logging
import akka.io.IO
import spray.can.Http


object Boot extends App {
  implicit val system = ActorSystem ("spray-api-service")
  val log = Logging (system, getClass)

  val service = system.actorOf (Props [SprayApiDemoServiceActor], "spray-service")

  IO (Http) ! Http.Bind (service, interface = "localhost", port = 8080)
}





















