(ns clj-pg-query.core
  (:require [clj-pg-query.utils :as utils]
            [clj-time.core :as tc]
            [clj-time.format :as tf]
            [clj-time.local :as tl]
            [clj-time.coerce :as tcc]
            [clojure.string :as str]
            [taoclj.foundation :as pg]
            [taoclj.foundation.execution :as execution])
  (:gen-class))


(def datasource (atom {}))

(defn set-datasource-config
  "Set a JDBC datasource config"
  {:added "1.0.0"}
  [config]
  (pg/def-datasource db config)
  (reset! datasource db))

(defn execute-raw-query
  "Execute raw SQL command"
  {:added "1.1.0"}
  [query]
  (if (instance? com.impossibl.postgres.jdbc.PGDataSource @datasource)
    (let [result (with-open [cnx (.getConnection @datasource)]
                   (execution/execute cnx query))] 
      (if (seq result)
        result
        ()))
    nil))

(defn execute-select
  "Execute SELECT statement"
  {:added "1.0.0"}
  [table where]
  (if (instance? com.impossibl.postgres.jdbc.PGDataSource @datasource)
    (let [result (pg/qry-> @datasource
                           (pg/select table where))]
      (if (seq? result)
        result
        ()))
    nil))

(defn execute-insert
  "Execute INSERT statement"
  {:added "1.0.0"}
  [table data]
  (if (instance? com.impossibl.postgres.jdbc.PGDataSource @datasource)
    (pg/qry-> @datasource
              (pg/insert table data))
    nil))

(defn execute-update
  "Execute UPDATE statement"
  {:added "1.0.0"}
  [table clause]
  (let [columns (:columns clause)
        where (:where clause)]
    (println clause)
    (println columns)
    (println where)
    (if (instance? com.impossibl.postgres.jdbc.PGDataSource @datasource)
      (pg/qry-> @datasource
                (pg/update table columns where))
      nil)))

(defn execute-delete
  "Execute DELETE statement"
  {:added "1.0.0"}
  [table where]
  (if (instance? com.impossibl.postgres.jdbc.PGDataSource @datasource)
    (pg/qry-> @datasource
              (pg/delete table where))
    nil))
