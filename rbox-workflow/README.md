# 推推棒模块

# MAVEN

## 配置1

### 全局settings.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
     <mirrors>
        <mirror>
            <id>alimaven</id>
            <name>aliyun maven</name>
            <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
            <mirrorOf>central</mirrorOf>
        </mirror>
    </mirrors> 
</settings>
```

### 项目pom文件中:
```xml
    <repositories>
        <repository>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
            <id>public</id>
            <name>public</name>
            <url>http://maven.ruigu.com/repository/maven-public/</url>
        </repository>
        <repository>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
            <id>activiti-releases</id>
            <name>activiti-releases</name>
            <url>http://maven.ruigu.com/repository/activiti-releases/</url>
        </repository>
        <!-- Activiti Releases 不要删除-->
        <!--        <repository>-->
        <!--            <id>alfresco</id>-->
        <!--            <name>Activiti Releases</name>-->
        <!--            <url>https://artifacts.alfresco.com/nexus/content/repositories/activiti-releases/</url>-->
        <!--            <releases>-->
        <!--                <enabled>true</enabled>-->
        <!--            </releases>-->
        <!--        </repository>-->
    </repositories>
```

## 配置2
如果公司私服中,配了activiti-releases的代理仓库 ,并添加到maven-public组中则可以只配置全局settings.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <mirrors>
        <mirror>
            <id>central</id>
            <name>rbox maven</name>
            <url>http://maven.ruigu.com/repository/maven-public/</url>
            <mirrorOf>*</mirrorOf>
        </mirror>
    </mirrors>
    <profiles>
        <profile>
            <id>rbox-nexus</id>
            <repositories>
                <repository>
                    <id>nexus_snapshot_repository</id>
                    <releases>
                        <enabled>false</enabled>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                    <url>http://maven.ruigu.com/repository/maven-snapshots/</url>
                    <layout>default</layout>
                </repository>
                <repository>
                    <id>central</id>
                    <url>http://maven.ruigu.com/repository/maven-public</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                </repository>
            </repositories>
            <pluginRepositories>
                <pluginRepository>
                    <id>central</id>
                    <url>http://maven.ruigu.com/repository/maven-public</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                    <updatePolicy>always</updatePolicy>
                </pluginRepository>
            </pluginRepositories>
        </profile>
    </profiles>
    <activeProfiles>
        <activeProfile>rbox-nexus</activeProfile>
    </activeProfiles>
</settings>
```


