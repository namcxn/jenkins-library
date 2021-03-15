
package libraries.kubernetes

void call(app_env) {
  stage "Deploy to ${app_env.cluster_name}", {

  def env  = app_env.environment ?:
            {error "Application Environment"}()

  def cluster_name = app_env.cluster_name ?:
                     config.cluster_name ?:
                     {error "k8s cluster not defined in library config or application environment config"}()

  def cluster_zone = app_env.cluster_zone ?:
                     config.cluster_zone ?:
                     {error "k8s cluster not defined in library config or application environment config"}()

  def project_id =  app_env.project_id ?:
                    config.project_id ?:
                    {error "k8s cluster not defined in library config or application environment config"}()

  def app_cred = app_env.app_cred ?:
                 config.project_id ?:
                 {error "k8s cluster not defined in library config or application environment config"}()

  /*
       helm release name.
       will use "release_name" if present on app env object
       or will use "short_name" if present on app_env object.
       will fail otherwise.
    */
  def release = app_env.release_name ?:
                "${JOB_NAME}" ?:
                {error "App Env Must Specify release_name"}()

  /*
    helm chart version
  */
  def chart_ver = app_env.release_ver ?:
                  "0.0.1" ?:
                  {error "App Env Must Specify release_ver"}()

   /*
      // k8s context
  def k8s_context = app_env.k8s_context ?:
                    config.k8s_context            ?:
                    {error "Kubernetes Context Not Defined"}()
    */

  def images = get_images_to_build()
  images.each { img ->
    sh "gcloud auth activate-service-account --key-file=${app_cred}"
    sh "gcloud container clusters get-credentials ${cluster_name} --zone ${cluster_zone} --project ${project_id}"
    sh label: 'Deploy to development', script: '''
                                kubectl cluster-info
                                helm repo add skymavis https://charts.skymavis.one
                                helm repo update
                                '''
    sh "helm upgrade --atomic --install ${release} skymavis/${release} --version ${chart_ver} --namespace ${env} --set-string image.repository=${img.repo} --set-string image.tag=${img.tag} --debug --dry-run"
    // helm secrets upgrade --atomic --install ${release} skymavis/${release} --version ${chart_ver} --namespace ${env} --set-string image.repository=${img.repo} --set-string image.tag=${img.tag} -f helm_vars/${env}/values.yaml -f helm_vars/${env}/secrets.yaml
     }
  }
      
  //  sh "gcloud auth activate-service-account --key-file=${app_cred}"
  //  sh "gcloud container clusters get-credentials ${cluster_name} --zone ${cluster_zone} --project ${project_id}"
  //  sh label: 'Deploy to development', script: '''
  //                               kubectl cluster-info
  //                               helm repo add skymavis https://charts.skymavis.one
  //                               helm repo update
  //                               '''
  //                               // helm secrets upgrade --atomic --install ${release} skymavis/${release} --version ${chart_ver} --namespace ${env} --set-string image.repository=${GCR_URL}/${PROJECT_NAME} --set-string image.tag=${BUILD_TAG} -f helm_vars/${env}/values.yaml -f helm_vars/${env}/secrets.yaml
  // }
}