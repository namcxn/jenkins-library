**Example**

```
libraries {
  ssh {
    ssh_credential = "ssh_credential_dev" 
  }
}

application_environments {
    dev {
        long_name = "dev"
        instance{
            user = "staging"
            ip = "35.232.83.71"
        }
    }
    prod {
      	long_name = "prod"
        instance{
            user = "production"
            ip = "1.2.3.5"
            ssh_credential = "ssh_credentail_prod"
        }
    }
}
```
