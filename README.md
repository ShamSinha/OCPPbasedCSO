# OCPPbasedCSO

JavaFX operator tool for basic Charging Station Operator tasks.

The useful path today is RFID user management:

1. Start/deploy `ocppCSMS`.
2. Open this CSO app and connect to:

```text
ws://localhost:8080/CSMSWebsocketServer-1/CSO
```

3. Add RFID UIDs or PINs in the RFID Users panel.
4. `OCPPcharger` sends normal OCPP `Authorize` messages to `ocppCSMS`.
5. `ocppCSMS` accepts or rejects authorization based on the CSO-managed token list.

## Admin Messages

The CSO app uses a small JSON admin protocol with `ocppCSMS`; the charger still uses OCPP-J.

```json
{"type":"RfidUsersList"}
{"type":"RfidUserUpsert","idToken":"04AABBCC","tokenType":"ISO14443","userName":"Demo Driver","status":"Accepted"}
{"type":"RfidUserDelete","idToken":"04AABBCC"}
```

The token concepts mirror the OCPP 2.1 authorization schema: `AuthorizeRequest.idToken` contains an `idToken` plus a `type`, and the CSMS replies with `idTokenInfo.status`.

## Build

```bash
./gradlew compileJava
```

Run:

```bash
./gradlew run
```
