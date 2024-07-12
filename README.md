# flex-slack-metrikker

## Kjøre mot bigquery lokalt

Kjør `gcloud auth application-default login` for å skape en service account som kan brukes til å koble opp mot bigquery lokalt.
Kjør så main med følgende miljøvariabler:


F.eks.:

```
GOOGLE_APPLICATION_CREDENTIALS=/Users/havard/.config/gcloud/application_default_credentials.json
GOOGLE_CLOUD_PROJECT=flex-prod-af40
```

Prosjekt id må være et sted din bruker har bigquery tilgang. F.eks. ditt dev prosjekt.

## Kjøre mot slack lokalt
For å kjøre mot slack lokalt må du ha `SLACK_TOKEN` miljøvariabelen satt. Denne kan du finne i secreten i prod namespacet.
Du må også set en slack channel id i `DAILY_SLACK_CHANNEL` miljøvariabelen. Denne finner du i slack under about i en kanals innstillinger.
Appen du bruker må ha tilgang til denne kanalen ved å være lagt til som integrasjon.