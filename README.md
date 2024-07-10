# flex-slack-metrikker

## Kjøre mot bigquery lokalt

Kjør `gcloud auth application-default login` for å skape en service account som kan brukes til å koble opp mot bigquery lokalt.
Kjør så main med følgende miljøvariabler:

```

F.eks.:

```
GOOGLE_APPLICATION_CREDENTIALS=/Users/havard/.config/gcloud/application_default_credentials.json
GOOGLE_CLOUD_PROJECT=flex-dev
```

Prosjekt id må være et sted din bruker har bigquery tilgang. F.eks. ditt dev prosjekt.
