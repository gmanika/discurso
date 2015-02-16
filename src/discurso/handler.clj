(ns discurso.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [taoensso.nippy :as nippy]
            [clojure.java.io :as io]
            [discurso.core :as discurso])
  (:import java.io.DataInputStream))

(defn read-file []
 (with-open [r (io/input-stream (.getFile (io/resource "cache-merge.nippy")))]
   (nippy/thaw-from-in! (DataInputStream. r))))

(def data (read-file))


(defroutes app-routes
  (GET "/" [] (str (discurso/build-paragraph data))))

(def app
  (handler/site app-routes))
