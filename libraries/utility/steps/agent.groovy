package libraries.utility 

void call(Closure body) {
  podTemplate(containers: [
    containerTemplate(name: 'docker-dind-gcloud', image: 'axieinfinity/axie-docker-dind-gcloud:latest', ttyEnabled: true, privileged: true, command: 'cat'),
  ]) {
    node(POD_LABEL, body)
  }
}