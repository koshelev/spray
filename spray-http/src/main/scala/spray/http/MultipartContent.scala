/*
 * Copyright © 2011-2013 the spray project <http://spray.io>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spray.http

/**
 * Basic model for multipart content as defined in RFC 2046.
 * If you are looking for a model for `multipart/form-data` you should be using [[spray.http.MultipartFormData]].
 */
case class MultipartContent(parts: Seq[BodyPart])

object MultipartContent {
  val Empty = MultipartContent(Nil)
}

/**
 * Model for one part of a multipart message.
 */
case class BodyPart(entity: HttpEntity, headers: Seq[HttpHeader] = Nil) {
  val name: Option[String] = dispositionParameterValue("name")

  def filename: Option[String] = dispositionParameterValue("filename")
  def disposition: Option[String] =
    headers.collectFirst {
      case disposition: HttpHeaders.`Content-Disposition` ⇒ disposition.dispositionType
    }

  def dispositionParameterValue(parameter: String): Option[String] =
    headers.collectFirst {
      case HttpHeaders.`Content-Disposition`("form-data", parameters) if parameters.contains(parameter) ⇒
        parameters(parameter)
    }
}
