# Course Registration DevOps Assessment

## 1. Project Overview
This project demonstrates course registration validation, automated testing, Maven multi-module builds, Jenkins CI/CD configuration, and Kubernetes deployment.

## 2. Registration Validation
- Reject registration when a course has reached capacity.
- Accept registration when one seat remains.
- Allow courses with 1 to 6 credits, inclusive.
- Reject courses with 0, negative, or more than 6 credits.
- Rejected registrations must not modify student or course enrollment state.

## 3. Project Structure
- `pom.xml` — Parent Maven POM and shared configuration.
- `course-registration-app/` — Java application module, tests, and build artifact.
- `Jenkinsfile` — Declarative Jenkins pipeline.
- `deployment.yaml` — Nginx Kubernetes Deployment.
- `service.yaml` — Kubernetes NodePort Service.

## 4. Build and Test
Run these commands from the repository root:

```powershell
mvn clean test
mvn package
```

The packaged JAR is generated under:
`course-registration-app/target/`

JUnit XML reports are generated under:
`course-registration-app/target/surefire-reports/`

## 5. Jenkins CI/CD
The Jenkinsfile defines stages to:
1. Verify Java and Maven.
2. Clean and run tests.
3. Package the application.
4. Archive the generated JAR.

JUnit reports are published from the Surefire reports directory. Packaging and artifact archiving run only after the test stage succeeds.

To use this pipeline, configure a Jenkins job that checks out this repository and runs the root Jenkinsfile on a Windows agent with Java and Maven installed.

## 6. Kubernetes Deployment
Deploy Nginx with two replicas:

```powershell
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
kubectl get deployments
kubectl get services
kubectl get pods -o wide
```

The Deployment uses image `nginx:1.27`. The NodePort Service exposes port `30080`.

Scale to three replicas:

```powershell
kubectl scale deployment nginx-deployment --replicas=3
kubectl rollout status deployment/nginx-deployment
kubectl get pods
```

Demonstrate self-healing by deleting one pod using its actual name from `kubectl get pods`:

```powershell
kubectl delete pod <actual-pod-name>
kubectl get pods -w
```

Kubernetes should create a replacement pod to maintain the desired replica count. Stop watching with `Ctrl+C`.

## 7. Verification Evidence
Capture screenshots of:
- Git commit history, merge into `main`, and clean working tree.
- Maven test results and successful package build.
- Generated JAR file and Surefire test reports.
- Jenkins pipeline and test results, if the pipeline is executed.
- Kubernetes Deployment, Service, three running pods, and pod replacement.

## 8. Limitations
The Jenkinsfile is provided as pipeline configuration. A successful Jenkins run must be verified separately in Jenkins. Kubernetes deployment evidence applies to the local Minikube environment.
