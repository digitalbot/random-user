(ns random-user.core
  (:require [http.async.client :as http]
            [clojure.data.json :as json]))

(defn get-random-dummy-users-json
  "get multi user vector"
  [n]
  (let [url "http://api.randomuser.me/"
        client (http/create-client)]
    (map (fn [res]
           (-> res http/await http/string))
         (for [i (range n)] (http/GET client url)))
    ))

(defn get-random-dummy-user-json
  "get user from http://randomuser.me/"
  []
  (with-open [client (http/create-client)]
    (let [resp (http/GET client "http://api.randomuser.me/" :timeout 500)]
      (http/await resp)
      (if (http/failed? resp)
        (println "error")
        (http/string resp)))))

(defn get-random-dummy-user
  "get user from http://randomuser.me/"
  []
  (let [resp (get-random-dummy-user-json)]
    (if (= nil resp)
      nil
      (json/read-str resp))))
