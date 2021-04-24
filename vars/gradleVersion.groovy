def call(String properties = "properties", env, String env_property) {
    def data = sh(returnStdout: true, script: "./gradlew ${properties} -q").trim().toString()
    def props = [:]
    data.split('\n').each { line -> line.tokenize(':').with { if (it.size() == 2) props[it[0].trim()] = it[1].trim() }}
    env[env_property] = props.get('version', 'Missing')
}