def call(Script script, String properties = "properties", String env_property = "MY_VERSION") {
    def data = sh(returnStdout: true, script: "./gradlew ${properties} -q").trim().toString()
    def props = [:]
    data.split('\n').each { line -> line.tokenize(':').with { if (it.size() == 2) props[it[0].trim()] = it[1].trim() }}
    script.env[env_property] = props.get('version', 'Missing')
}