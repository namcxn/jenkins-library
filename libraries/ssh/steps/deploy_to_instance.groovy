package libraries.ssh 

void call(app_env) {
  stage("Deploy to ${app_env.long_name}") {
    def remote = [:]
    remote.name = app_env.long_name
    remote.host = app_env.ip
    remote.allowAnyHosts = true
    /*
       ssh credential 
    */
    def ssh_credential = app_env.ssh_credential ?:
                         config.ssh_credential ?:
                         {error "SSH credentail not found"}()

    withCredentials([sshUserPrivateKey(credentialsId: ssh_credential, 
            keyFileVariable: 'identityFile',
            usernameVariable: 'userName' )]){
      remote.user = userName
      remote.identityFile = identityFile
      String release = config.method_release ?: "command"
      if ( release == "command") {
        echo "Release to DEV"
        //command_release()
      }
    }
  }
}

void command_release() {

  String

  sshCommand remote: remote, command: "${docker_command}"
}

void execute_release() {

}