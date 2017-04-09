package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.iteratee._
import play.api.Logger
import play.api.libs.oauth.{ConsumerKey, RequestToken}
import play.api.libs.oauth._
import play.api.Play.current
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws._
import actors.TwitterStreamer
import play.api.Play.current
import play.api.libs.json._
import play.api.i18n.Messages.Implicits._


class Application extends Controller {
/*def tweets = Action {
Ok
}
}*/


/*def tweets = Action.async {
val credentials: Option[(ConsumerKey, RequestToken)] = for {
apiKey <- Play.configuration.getString("twitter.apiKey")
apiSecret <- Play.configuration.getString("twitter.apiSecret")
token <- Play.configuration.getString("twitter.token")
tokenSecret <- Play.configuration.getString("twitter.tokenSecret")
} yield (
ConsumerKey(apiKey, apiSecret),
RequestToken(token, tokenSecret)
)

credentials.map { case (consumerKey, requestToken) =>
Future.successful {
Ok
}
} getOrElse {
Future.successful {
InternalServerError("Twitter credentials missing")
}
}
}*/


/*def tweets = Action.async {
val loggingIteratee = Iteratee.foreach[Array[Byte]] { array =>
Logger.info(array.map(_.toChar).mkString)
}
credentials.map { case (consumerKey, requestToken) =>
WS
.url("https://stream.twitter.com/1.1/statuses/filter.json")
.sign(OAuthCalculator(consumerKey, requestToken))
.withQueryString("track" -> "cat")
.get { response => 
Logger.info("Status: " + response.status)
loggingIteratee
}.map { _ =>
Ok("Stream closed")
}
}

def credentials: Option[(ConsumerKey, RequestToken)] = for {
apiKey <- Play.configuration.getString("twitter.apiKey")
apiSecret <- Play.configuration.getString("twitter.apiSecret")
token <- Play.configuration.getString("twitter.token")
tokenSecret <- Play.configuration.getString("twitter.tokenSecret")
} yield (
ConsumerKey(apiKey, apiSecret),
RequestToken(token, tokenSecret)
)
}*/

def index = Action { implicit request =>
Ok(views.html.index("Tweets"))
}

def tweets = WebSocket.acceptWithActor[String, JsValue] {
request => out => TwitterStreamer.props(out)
}

def replicateFeed = Action { implicit request =>
Ok.feed(TwitterStreamer.subscribeNode)
}
}