
package libraries.kubernetes

void call(app_env) {
  stage "Deploy to ${app_env}" {

  def cluster_name = app_env.cluster_name ?:
                     config.cluster_name ?:
                     {error "k8s cluster not defined in library config or application environment config"}()

  def cluster_zone = app_env.cluster_zone ?:
                     config.cluster_zone ?:
                     {error "k8s cluster not defined in library config or application environment config"}()

  def project_id =  app_env.project_id ?:
                    config.project_id ?:
                    {error "k8s cluster not defined in library config or application environment config"}()

   sh "gcloud container clusters get-credentials ${cluster_name} --zone ${cluster_zone} --project ${project_id}"
   sh label: 'Deploy to development', script: '''
                                kubectl cluster-info
                                helm repo add skymavis https://charts.skymavis.one
                                helm repo update
                                helm secrets upgrade --atomic --install ${JOB_NAME} skymavis/${JOB_NAME} --version ${HELM_CHART_VERSION} --namespace ${NAMESPACE} --set-string image.repository=${GCR_URL}/${PROJECT_NAME} --set-string image.tag=${BUILD_TAG} -f helm_vars/${ENV}/values.yaml -f helm_vars/${ENV}/secrets.yaml
                                '''
  }
}