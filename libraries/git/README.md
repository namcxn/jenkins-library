*Full example using keywords*
[source,groovy]
---
on_commit{
  gitlab_status("connection1", "service-account", "running")
  continuous_integration()
  gitlab_status("connection1", "service-account", "success")
}

on_pull_request to: develop, {
  gitlab_status("connection2", "service-account", "pending")
  continuous_integration()
  gitlab_status("connection2", "service-account", "running")
  deploy_to dev
  parallel "508 Testing": { accessibility_compliance_test() },
          "Functional Testing": { functional_test() },
          "Penetration Testing": { penetration_test() }
  deploy_to staging
  performance_test()
  gitlab_status("connection2", "service-account", "success")
}

on_merge to: master, from: develop, {
  gitlab_status("connection", "service-account2", "running")
  deploy_to prod
  smoke_test()
  gitlab_status("connection", "service-account2", "success")
}
---