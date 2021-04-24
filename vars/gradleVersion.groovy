def call(String properties = "properties") {
    def data = sh(returnStdout: true, script: "./gradlew ${properties} -q").trim().toString()
    def props = [:]
    data.splitEachLine { props[it[0]] = it[1]}
    return props.get("version", "Missing")
}