
package libraries.docker

void call(String _url = null, String _credentialId = null, def body){

  def (repository, cred) = get_registry_info()

  String protocol = config.registry_protocol ?: "https://"
  String url = _url ?: "${protocol}${repository}"
  String credentialId = "gcr:${_credentialId} ?: ${cred}"

  docker.withRegistry(url, credentialId, body)

}
