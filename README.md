# farolapi

## Arranque 

```sh
docker run --name farolapi cbadenes/farolapi
```

FarolApi necesita el servicio de mensajería RabbitMQ, el motor de consenso y un SparQL endpoint. Para establecer cada uno de estos servicios puedes utilizar las siguientes variables de entorno: 

```sh
docker run -d --restart always --name farolapi -e FAROLAPI_BUS=rabbitmq -e FAROLAPI_CONSENSUS=consensus:5001/tmp -e FAROLAPI_VIRTUOSO=virtuoso:8890/sparql cbadenes/farolapi
```


## Consultas


1. Listado de farolas dado un area de localización:

   GET `<farolapp-uri>/lampposts?lat1=0.1&long1=0.1&lat2=0.2&long2=0.2`

   ```json
   {
   	"lampposts": [{
   		"id": "4bf8065a-2107-11e6-b67b-9e71128cae77",
   		"lat": 40.345583,
   		"long": -3.839938,
   		"radius": 12,
   		"color": "white",
   		"type": "a",
   		"pollution": "low"
   	}, {
   		"id": "53778860-2107-11e6-b67b-9e71128cae77",
   		"lat": 40.340083,
   		"long": -2.839938,
   		"radius": 8,
   		"color": "blue",
   		"type": "b",
   		"pollution": "high"
   	}]
   }
   ```

2. Listado de farolas dado un area de localización y un tiempo de cambio:

   GET `<farolapp-uri>/lampposts?lat1=0.1&long1=0.1&lat2=0.2&long2=0.2&time=5m`

   ```json
   {
   	"lampposts": [{
   		"id": "4bf8065a-2107-11e6-b67b-9e71128cae77",
   		"lat": 40.345583,
   		"long": -3.839938,
   		"radius": 12,
   		"color": "white",
   		"type": "a",
   		"pollution": "low"
   	}, {
   		"id": "53778860-2107-11e6-b67b-9e71128cae77",
   		"lat": 40.340083,
   		"long": -2.839938,
   		"radius": 8,
   		"color": "blue",
   		"type": "b",
   		"pollution": "high"
   	}]
   }
   ```

3. Detalle de una farola:

   GET `<farolapp-uri>/lampposts/<id>`

   ```json
   {
   	"lat": 40.345583,
   	"long": -3.839938,
   	"wattage": {
   		"value": "low",
   		"range": ["low", "medium", "high"]
   	},
   	"lamp": {
   		"value": "VSAP",
   		"range": ["VSAP", "VMCC", "VMAP", "PAR", "MC", "LED", "I", "H", "VSBP", "FCBC", "HM", "F"]
   	},
   	"height": {
   		"value": "high",
   		"range": ["low", "medium", "high"]
   	},
   	"light": {
   		"value": "P",
   		"range": ["P", "F", "E", "AA", "AC", "ER", "O"]
   	},
   	"color": {
   		"value": "white",
   		"range": ["red", "orange", "yellow", "white", "green", "blue"]
   	},
   	"covered": {
   		"value": "true",
   		"range": ["true", "false"]
   	},
   	"status": {
   		"value": "blown",
   		"range": ["blown", "damaged", "works"]
   	},
   	"pollution": {
   		"value": "low",
   		"range": ["low", "medium", "high"]
   	},
   	"streetViewPov": {
   		"heading": 0,
   		"pitch": 0
   	}
   }
   ```

4. Anotar una farola:

   POST `<farolaapp-uri>/lampposts/<id>/annotations`

   ```json
   {
   	"wattage": "low",
   	"lamp": "VSAP",
   	"height": "high",
   	"light": "P",
   	"color": "white",
   	"covered": "true",
   	"status": "blown",
    "streetViewPov": {
        "heading": 0,
        "pitch": 0
    }
   }
   ```