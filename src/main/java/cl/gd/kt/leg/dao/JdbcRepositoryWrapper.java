package cl.gd.kt.leg.dao;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;
import java.util.Optional;

public class JdbcRepositoryWrapper {

    private final SQLClient client;

    public JdbcRepositoryWrapper (Vertx vertx, JsonObject config) {
        this.client = PostgreSQLClient.createShared(vertx, config);

    }

    /**
     * Suitable for `add`, `exists` operation.
     *
     * @param params        query params
     * @param sql           sql
     * @param resultHandler async result handler
     */
    protected void executeNoResult(JsonArray params, String sql, Handler<AsyncResult<Void>> resultHandler) {

        client.getConnection(connHandler(resultHandler, connection -> connection.updateWithParams(sql, params, r -> {
            connection.close();
            if (r.succeeded()) {
                resultHandler.handle(Future.succeededFuture());
            } else {

                resultHandler.handle(Future.failedFuture(r.cause()));

            }
        })));
    }

    protected void executeOneResult(JsonArray params, String sql, Handler<AsyncResult<Optional<JsonObject>>> resultHandler) {
        client.getConnection(connHandler(resultHandler, connection -> connection.queryWithParams(sql, params, r -> {
            connection.close();
            if (r.succeeded()) {
                List<JsonObject> resList = r.result().getRows();
                if (resList == null || resList.isEmpty()) {
                    resultHandler.handle(Future.succeededFuture(Optional.empty()));

                } else {
                    resultHandler.handle(Future.succeededFuture(Optional.of(resList.get(0))));
                }

            } else {
                resultHandler.handle(Future.failedFuture(r.cause()));
            }
        })));
    }

    protected void retrieveOneResultWithoutParams(String sql, Handler<AsyncResult<JsonObject>> resultHandler) {
        client.getConnection(connHandler(resultHandler, connection -> connection.query(sql, r -> {
            connection.close();
            if(r.succeeded()) {
                JsonObject res = r.result().getRows().get(0);
                resultHandler.handle(Future.succeededFuture(res));
            } else {
                resultHandler.handle(Future.failedFuture(r.cause()));
            }
        })));
    }

    protected void retrieveOneResultWithParams(JsonArray params, String sql, Handler<AsyncResult<JsonObject>> resultHandler) {
        client.getConnection(connHandler(resultHandler, connection -> connection.queryWithParams(sql, params, r -> {
            connection.close();
            if(r.succeeded()) {
                JsonObject res = r.result().getRows().get(0);
                resultHandler.handle(Future.succeededFuture(res));
            } else {
                resultHandler.handle(Future.failedFuture(r.cause()));
            }
        })));
    }

    protected <K> Future<Optional<JsonObject>> retrieveOne(K param, String sql) {
        return getConnection()
                .compose(connection -> {
                    Future<Optional<JsonObject>> future = Future.future();
                    connection.queryWithParams(sql, new JsonArray().add(param), r -> {
                        connection.close();
                        if (r.succeeded()) {
                            List<JsonObject> resList = r.result().getRows();
                            if (resList == null || resList.isEmpty()) {
                                future.complete(Optional.empty());

                            } else {
                                future.complete(Optional.of(resList.get(0)));
                            }
                        } else {

                            future.fail(r.cause());
                        }
                    });
                    return future;
                });
    }

    private int calcOffset(int page, int limit) {
        if (page <= 0)
            return 0;
        return limit * (page - 1);
    }

    protected Future<List<JsonObject>> retrieveByPage(int page, int limit, String sql) {
        JsonArray params = new JsonArray().add(limit).add(calcOffset(page, limit));
        return getConnection().compose(connection -> {
            Future<List<JsonObject>> future = Future.future();
            connection.queryWithParams(sql, params, r -> {
                connection.close();
                if (r.succeeded()) {
                    future.complete(r.result().getRows());
                } else {

                    future.fail(r.cause());
                }
            });
            return future;
        });
    }



    protected Future<List<JsonObject>> retrieveMany(JsonArray param, String sql) {
        return getConnection().compose(connection -> {
            Future<List<JsonObject>> future = Future.future();
            connection.queryWithParams(sql, param, r -> {
                connection.close();
                if (r.succeeded()) {
                    future.complete(r.result().getRows());
                } else {
                    future.fail(r.cause());
                }
            });
            return future;
        });
    }

    protected Future<List<JsonObject>> retrieveAll(String sql) {
        return getConnection().compose(connection -> {
            Future<List<JsonObject>> future = Future.future();
            connection.query(sql, r -> {
                connection.close();
                if (r.succeeded()) {
                    future.complete(r.result().getRows());
                } else {
                    future.fail(r.cause());
                }
            });
            return future;
        });
    }

    protected <K> void removeOne(K id, String sql, Handler<AsyncResult<Void>> resultHandler) {
        client.getConnection(connHandler(resultHandler, connection -> {
            JsonArray params = new JsonArray().add(id);
            connection.updateWithParams(sql, params, r -> {
                connection.close();
                if (r.succeeded()) {
                    resultHandler.handle(Future.succeededFuture());
                } else {
                    resultHandler.handle(Future.failedFuture(r.cause()));
                }

            });
        }));
    }

    protected void removeAll(String sql, Handler<AsyncResult<Void>> resultHandler) {
        client.getConnection(connHandler(resultHandler, connection -> connection.update(sql, r -> {
            connection.close();
            if (r.succeeded()) {
                resultHandler.handle(Future.succeededFuture());
            } else {

                resultHandler.handle(Future.failedFuture(r.cause()));
            }
        })));
    }

    /**
     * A helper methods that generates async handler for SQLConnection
     *
     * @return generated handler
     */
    private <R> Handler<AsyncResult<SQLConnection>> connHandler(Handler<AsyncResult<R>> h1, Handler<SQLConnection> h2) {
        return conn -> {
            if (conn.succeeded()) {
                final SQLConnection connection = conn.result();
                h2.handle(connection);
            } else {
                h1.handle(Future.failedFuture(conn.cause()));
            }
        };
    }


    private Future<SQLConnection> getConnection() {
        Future<SQLConnection> future = Future.future();
        client.getConnection(future.completer());
        return future;
    }
}
