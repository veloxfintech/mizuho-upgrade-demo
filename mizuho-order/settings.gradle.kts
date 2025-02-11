pluginManagement {
    // defines where to search for plugins
    repositories {
        mavenLocal()  // No authentication needed here
        gradlePluginPortal()
        maven {
            url = uri(providers.gradleProperty("velox_repo_url").get())
            credentials {
                username = providers.gradleProperty("velox_repo_user").get()
                password = providers.gradleProperty("velox_repo_password").get()
            }
        }
    }
}

rootProject.name = "mizuho-order"
