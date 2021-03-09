package libraries.utility 

void call(Closure body) {
  podTemplate(cloud: 'kubernetes', containers: [
    containerTemplate(name: 'docker-dind-gcloud', image: 'axieinfinity/axie-docker-dind-gcloud:latest', ttyEnabled: true, privileged: true, command: 'cat'),
    containerTemplate(name: 'helm-gcloud', image: 'axieinfinity/axie-helm3-gcloud:latest', ttyEnabled: true, privileged: true, command: 'cat'),
  ],
  volumes: [
    emptyDirVolume(mountPath: "/var/lib/docker", memory: false),
    hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock'),
    configMapVolume(mountPath: '/root/.gcloud/', configMapName: 'helm-secrets-confimap'),
  ]) {
    node(POD_LABEL) {
      body()
    }
  }
}