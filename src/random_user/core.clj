(ns random-user.core
  (:require [http.async.client :as http]
            [clojure.data.json :as json]))

(def api-endpoint "http://api.randomuser.me/")

(defn request
  "get method wrapper"
  [client]
  (http/GET client api-endpoint))

(defn get-random-dummy-users
  "get multi users as vector"
  [n]
  (with-open [client (http/create-client)]
    (let [resps (keep (fn [resp]
                        (http/await resp)
                        (if-not (http/failed? resp)
                          (-> resp http/string json/read-str)))
                      (for [_ (range n)] (request client)))]
      (vec (doall resps)))))

(defn get-random-dummy-user-json
  "get user from http://randomuser.me/ as json"
  []
  (with-open [client (http/create-client)]
    (let [resp (request client)]
      (http/await resp)
      (if-not (http/failed? resp)
        (http/string resp)))))

(defn get-random-dummy-user
  "get user from http://randomuser.me/"
  []
  (let [resp (get-random-dummy-user-json)]
    (if-not (nil? resp)
      (json/read-str resp))))
