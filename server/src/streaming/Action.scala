package monarchy.streaming

sealed trait Action

case object Ping extends Action
case object Pong extends Action

