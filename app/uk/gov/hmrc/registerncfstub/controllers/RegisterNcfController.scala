/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.registerncfstub.controllers

import java.time.Instant
import java.util.UUID

import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.json._
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.controller.BackendController
import uk.gov.hmrc.registerncfstub.config.AppConfig
import uk.gov.hmrc.registerncfstub.model.{Eis5xxError, _}
import uk.gov.hmrc.registerncfstub.services.RegisterNcfService
@Singleton
class RegisterNcfController @Inject()(appConfig: AppConfig, registerNcfService: RegisterNcfService, cc: ControllerComponents)
    extends BackendController(cc) {

  def receiveNcfData: Action[JsValue] = Action(parse.json) { implicit request =>
    implicit val correlationId: String = request.headers.get("X-Correlation-ID").getOrElse("-")

    request.body.validate[NcfRequestData] match {
      case JsSuccess(t, _) =>
        registerNcfService.processRegisterNcfRequest(t) match {
          case CompletedSuccessfully(mrn, responseCode) =>
            Logger.info(s"NCF returning response code $responseCode with HTTP status code 200 for MRN $mrn")
            responseWithCorrelationIdHeader(Ok(Json.toJson(NcfResponse(mrn, responseCode, None))))
          case TechnicalError(mrn, responseCode, e) =>
            logResponse(mrn, responseCode, e, 400)
            responseWithCorrelationIdHeader(BadRequest(Json.toJson(NcfResponse(mrn, responseCode, Some(e)))))
          case ParsingError(mrn, responseCode, e) =>
            logResponse(mrn, responseCode, e)
            responseWithCorrelationIdHeader(Ok(Json.toJson(NcfResponse(mrn, responseCode, Some(e)))))
          case InvalidMrn(mrn, responseCode, e) =>
            logResponse(mrn, responseCode, e)
            responseWithCorrelationIdHeader(Ok(Json.toJson(NcfResponse(mrn, responseCode, Some(e)))))
          case UnknownMrn(mrn, responseCode, e) =>
            logResponse(mrn, responseCode, e)
            responseWithCorrelationIdHeader(Ok(Json.toJson(NcfResponse(mrn, responseCode, Some(e)))))
          case InvalidStateOod(mrn, responseCode, e) =>
            logResponse(mrn, responseCode, e)
            responseWithCorrelationIdHeader(Ok(Json.toJson(NcfResponse(mrn, responseCode, Some(e)))))
          case InvalidStateOot(mrn, responseCode, e) =>
            logResponse(mrn, responseCode, e)
            responseWithCorrelationIdHeader(Ok(Json.toJson(NcfResponse(mrn, responseCode, Some(e)))))
          case InvalidCustomsOffice(mrn, responseCode, e) =>
            logResponse(mrn, responseCode, e)
            responseWithCorrelationIdHeader(Ok(Json.toJson(NcfResponse(mrn, responseCode, Some(e)))))
          case OotNotForCountry(mrn, responseCode, e) =>
            logResponse(mrn, responseCode, e)
            responseWithCorrelationIdHeader(Ok(Json.toJson(NcfResponse(mrn, responseCode, Some(e)))))
          case SchemaValidationError => returnSchemaValidationError
          case Eis5xxError =>
            Logger.info("NCF returning HTTP status code 500")
            responseWithCorrelationIdHeader(InternalServerError)
        }
      case JsError(_) =>
        returnSchemaValidationError
    }
  }

  private def returnSchemaValidationError()(implicit correlationId: String): Result = {
    Logger.info("NCF returning HTTP status code 400 because payload failed schema validation")

    responseWithCorrelationIdHeader(
      BadRequest(
        Json.obj(
          "errorDetail" -> Json.obj(
            "timestamp"         -> Json.toJson(Instant.now()),
            "correlationId"     -> UUID.randomUUID().toString,
            "errorCode"         -> "400",
            "errorMessage"      -> "Invalid message",
            "source"            -> "JSON validation",
            "sourceFaultDetail" -> Json.obj("detail" -> Json.toJson(Seq("Invalid json payload")))
          )
        )
      ))
  }

  private def responseWithCorrelationIdHeader(r: Result)(implicit correlationId: String): Result =
    r.withHeaders("X-Correlation-ID" -> correlationId)

  private def logResponse(mrn: String, responseCode: Int, errorDescription: String, httpStatusCode: Int = 200): Unit =
    Logger.info(s"""NCF returning response code ${responseCode.toString} with error "$errorDescription" and HTTP status code ${httpStatusCode.toString} for MRN $mrn""")
}
