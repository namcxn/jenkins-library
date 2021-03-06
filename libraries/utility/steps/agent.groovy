package libraries.utility 

void call(Closure body) {
  podTemplate(cloud: 'kubernetes', containers: [
    containerTemplate(name: 'docker-dind-gcloud', image: 'axieinfinity/axie-docker-dind-gcloud:latest', ttyEnabled: true, privileged: true, command: 'cat'),
  ],
  volumes: [
    emptyDirVolume(mountPath: "/var/lib/docker", memory: false),
    hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')
  ]) {
    node(POD_LABEL, body)
  }
}