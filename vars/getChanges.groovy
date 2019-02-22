def call(build) {
    return buildChangelog(build)
}

def makeHeader(build) {
    def ret = "Build: "
    if (build.buildVariables.MYVERSION != null)
        ret += build.buildVariables.MYVERSION
    else
        ret += build.number + " - NOVERSION"
    ret += " - ${new Date(build.startTimeInMillis)}"
    return ret
}

def buildChangelog(start) {
    def changelog = []
    def header = makeHeader(start)
    def build = start
    def changes = []
    //Loop here to not need recursion in case we get massive build lists
    while (build != null) {
        changes = addChanges(build, changes)
        build = build.previousBuild
        if (build == null || build.result == 'SUCCESS') {
            if (!changes.isEmpty()) {
                changelog += header
                changelog.addAll(changes)
                changes = []
                break
            }
        }
    }
    return changelog.join("\n")
}

def addChanges(build, changelog) {
    for (change in build.changeSets)
    {
        for (chg in change?.items)
        {
            def msg = getMessage(chg)
            if (!msg?.contains("\n"))
            {
                changelog += "\t${chg.author.toString()}: ${chg.msg}"
            }
            else
            {
                changelog += "\t${chg.author.toString()}:"
                for (pt in msg?.split('\n'))
                    changelog += "\t\t${pt}"
                changelog += ""
            }
        }
    }
    return changelog
}

def getMessage(change) {
    if (change.metaClass.respondsTo(change, 'getComment') || change.metaClass.hasProperty(change, 'comment'))
        return change.comment
    return change.msg
}
