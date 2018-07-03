package cl.gd.kt.leg.verticle;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import cl.gd.kt.leg.util.AppEnum;
import cl.gd.kt.leg.util.DataBaseEnum;
import cl.gd.kt.leg.util.SystemUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class RestApiVerticle extends AbstractVerticle {

	protected static final String INFORMATION_REQUIRED = "Information is required";

    private static final String CONTENT_TYPE = "content-type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";
    protected static final String ACCESS_CONTROL = "Access-Control-Allow-Origin";

    @Override
    public JsonObject config() {
        final String username = SystemUtil.getEnvironmentStrValue(DataBaseEnum.DB_USER.name());
        final String password = SystemUtil.getEnvironmentStrValue(DataBaseEnum.DB_PASSWORD.name());
        final String database = SystemUtil.getEnvironmentStrValue(DataBaseEnum.DB_DATABASE.name());
        final int maxPoolSize = SystemUtil.getEnvironmentIntValue(DataBaseEnum.DB_POOL_SIZE.name());
        final int queryTimeout = SystemUtil.getEnvironmentIntValue(DataBaseEnum.DB_QUERY_TIMEOUT.name());
        final String databaseHost = SystemUtil.getEnvironmentStrValue(DataBaseEnum.DB_HOST.name());
        final int appPort = SystemUtil.getEnvironmentIntValue(AppEnum.APP_PORT.name());
        return new JsonObject()
                .put("http.port", appPort)
                .put("username", username)
                .put("password", password)
                .put("maxPoolSize", maxPoolSize)
                .put("database", database)
                .put("host", databaseHost)
                .put("queryTimeout", queryTimeout);

    }

    /**
     * Create http server for the REST service.
     *
     * @param router router instance
     * @param port   http port
     * @return async result of the procedure
     */
    protected Future<Void> createHttpServer(Router router, int port) {
        Future<HttpServer> httpServerFuture = Future.future();
        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(port, httpServerFuture.completer());
        return httpServerFuture.map(r -> null);
    }

    /**
     * Enable CORS support.
     *
     * @param router router instance
     */
    protected void enableCorsSupport(Router router) {
        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add(ACCESS_CONTROL);
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("accept");
        allowHeaders.add("Authorization");

        Set<HttpMethod> allowMethods = new HashSet<>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.PUT);
        allowMethods.add(HttpMethod.OPTIONS);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PATCH);

        router.route().handler(CorsHandler.create("*")
                .allowedHeaders(allowHeaders)
                .allowedMethods(allowMethods));
    }

    // helper result handler within a request context

    /**
     * This method generates handler for async methods in REST APIs.
     */
    protected <T> Handler<AsyncResult<T>> resultHandler(RoutingContext context, Handler<T> handler) {
        return res -> {
            if (res.succeeded()) {
                handler.handle(res.result());
            } else {
                internalError(context, res.cause());
                log.error(res.cause().getCause().getMessage());
            }
        };
    }

    /**
     * This method generates handler for async methods in REST APIs.pmocstaging@pmoc-staging.ce9mpbn4hdii.us-east-1.rds.amazonaws.com
     * Use the result directly and invoke `toString` as the response. The content type is JSON.
     */
    protected <T> Handler<AsyncResult<T>> resultHandler(RoutingContext context) {
        return ar -> {
            if (ar.succeeded()) {
                T res = ar.result();
                context.response()
                        .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .putHeader(ACCESS_CONTROL, "*")
                        .end(res == null ? "{}" : res.toString());
            } else {
                internalError(context, ar.cause());
                log.error(ar.cause().getCause().getMessage());
            }
        };
    }

    /**
     * This method generates handler for async methods in REST APIs.
     * Use the result directly and use given {@code converter} to convert result to string
     * as the response. The content type is JSON.
     *
     * @param context   routing context instance
     * @param converter a converter that converts result to a string
     * @param <T>       result type
     * @return generated handler
     */
    protected <T> Handler<AsyncResult<T>> resultHandler(RoutingContext context, Function<T, String> converter) {
        return ar -> {
            if (ar.succeeded()) {
                T res = ar.result();
                if (res == null) {
                    serviceUnavailable(context, "invalid_result");
                } else {
                    context.response()
                            .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                            .putHeader(ACCESS_CONTROL, "*")
                            .end(converter.apply(res));
                }
            } else {
                internalError(context, ar.cause());
                log.error(ar.cause().getCause().getMessage());
            }
        };
    }

    /**
     * This method generates handler for async methods in REST APIs.
     * Use the result directly and use given {@code converter} to convert result to string
     * as the response. The content type is JSON.
     *
     * @param context   routing context instance
     * @param converter a converter that converts result to a string
     * @param <T>       result type
     * @return generated handler
     */
    protected <T> Handler<AsyncResult<T>> resultHandler(RoutingContext context, Function<T, String> converter, int statusCode) {
        return ar -> {
            if (ar.succeeded()) {
                T res = ar.result();
                if (res == null) {
                    serviceUnavailable(context, "invalid_result");
                } else {
                    context.response()
                    		.setStatusCode(statusCode)
                            .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                            .putHeader(ACCESS_CONTROL, "*")
                            .end(converter.apply(res));

                }
            } else {
                internalError(context, ar.cause());
                log.error(ar.cause().getCause().getMessage());
            }
        };
    }

    /**
     * This method generates handler for async methods in REST APIs.
     * The result requires non-empty. If empty, return <em>404 Not Found</em> status.
     * The content type is JSON.
     *
     * @param context routing context instance
     * @param <T>     result type
     * @return generated handler
     */
    protected <T> Handler<AsyncResult<T>> resultHandlerNonEmpty(RoutingContext context) {
        return ar -> {
            if (ar.succeeded()) {
                T res = ar.result();
                if (res == null) {
                    notFound(context);
                } else {
                    context.response()
                            .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                            .putHeader(ACCESS_CONTROL, "*")
                            .end(res.toString());
                }
            } else {
                internalError(context, ar.cause());
                log.error(ar.cause().getCause().getMessage());
            }
        };
    }

    /**
     * This method generates handler for async methods in REST APIs.
     * The content type is originally raw text.
     *
     * @param context routing context instance
     * @param <T>     result type
     * @return generated handler
     */
    protected <T> Handler<AsyncResult<T>> rawResultHandler(RoutingContext context) {
        return ar -> {
            if (ar.succeeded()) {
                T res = ar.result();
                context.response()
                        .putHeader(ACCESS_CONTROL, "*")
                        .end(res == null ? "" : res.toString());
            } else {
                internalError(context, ar.cause());
                log.error(ar.cause().getCause().getMessage());
            }
        };
    }

    protected Handler<AsyncResult<Void>> resultVoidHandler(RoutingContext context, JsonObject result) {
        return resultVoidHandler(context, result, 200);
    }

    /**
     * This method generates handler for async methods in REST APIs.
     * The result is not needed. Only the state of the async result is required.
     *
     * @param context routing context instance
     * @param result  result content
     * @param status  status code
     * @return generated handler
     */
    protected Handler<AsyncResult<Void>> resultVoidHandler(RoutingContext context, JsonObject result, int status) {
        return ar -> {
            if (ar.succeeded()) {
                context.response()
                        .setStatusCode(status == 0 ? 200 : status)
                        .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .putHeader(ACCESS_CONTROL, "*")
                        .end(result.encodePrettily());
            } else {
                internalError(context, ar.cause());
                log.error(ar.cause().getCause().getMessage());
            }
        };
    }

    protected Handler<AsyncResult<Void>> resultVoidHandler(RoutingContext context, int status) {
        return ar -> {
            if (ar.succeeded()) {
                context.response()
                        .setStatusCode(status == 0 ? 200 : status)
                        .putHeader(ACCESS_CONTROL, "*")
                        .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .end();
            } else {
                internalError(context, ar.cause());
                log.error(ar.cause().getCause().getMessage());
            }
        };
    }

    /**
     * This method generates handler for async methods in REST DELETE APIs.
     * Return format in JSON (successful status = 204):
     * <code>
     * {"message": "delete_success"}
     * </code>
     *
     * @param context routing context instance
     * @return generated handler
     */
    protected Handler<AsyncResult<Void>> deleteResultHandler(RoutingContext context) {
        return res -> {
            if (res.succeeded()) {
                context.response().setStatusCode(204)
                        .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .putHeader(ACCESS_CONTROL, "*")
                        .end(new JsonObject().put(MESSAGE, "delete_success").encodePrettily());
            } else {
                internalError(context, res.cause());
                log.error(res.cause().getCause().getMessage());
            }
        };
    }

    // helper method dealing with failure

    protected void badRequest(RoutingContext context, Throwable ex) {
        context.response().setStatusCode(400)
                .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                .putHeader(ACCESS_CONTROL, "*")
                .end(new JsonObject().put(ERROR, ex.getMessage()).put(MESSAGE,ex.getMessage()).encodePrettily());
    }

    protected void notFound(RoutingContext context) {
        context.response().setStatusCode(404)
                .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                .putHeader(ACCESS_CONTROL, "*")
                .end(new JsonObject().put(MESSAGE, "not_found").encodePrettily());
    }

    protected void internalError(RoutingContext context, Throwable ex) {
        context.response().setStatusCode(500)
                .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                .putHeader(ACCESS_CONTROL, "*")
                .end(new JsonObject().put(MESSAGE, ex.getMessage()).put(ERROR,ex.getMessage()).encodePrettily());
    }

    protected void notImplemented(RoutingContext context) {
        context.response().setStatusCode(501)
                .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                .putHeader(ACCESS_CONTROL, "*")
                .end(new JsonObject().put(MESSAGE, "not_implemented").encodePrettily());
    }

    protected void badGateway(Throwable ex, RoutingContext context) {
        log.error(ex.getMessage());
        context.response()
                .setStatusCode(502)
                .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                .putHeader(ACCESS_CONTROL, "*")
                .end(new JsonObject().put(ERROR, "bad_gateway")
                        //.put(MESSAGE, ex.getMessage())
                        .encodePrettily());
    }

    protected void serviceUnavailable(RoutingContext context) {
        context.fail(503);
    }

    protected void serviceUnavailable(RoutingContext context, Throwable ex) {
        context.response().setStatusCode(503)
                .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                .putHeader(ACCESS_CONTROL, "*")
                .end(new JsonObject().put(ERROR, ex.getMessage()).encodePrettily());
    }

    protected void serviceUnavailable(RoutingContext context, String cause) {
        context.response().setStatusCode(503)
                .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                .putHeader(ACCESS_CONTROL, "*")
                .end(new JsonObject().put(ERROR, cause).encodePrettily());
    }
}
