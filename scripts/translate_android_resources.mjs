import { readFile, writeFile } from 'node:fs/promises'
import { pathToFileURL } from 'node:url'

const { LlmClient, messageText } = await import(
  pathToFileURL('/home/ubuntu/skills/builtin-llm-models/lib/llm.mjs').href,
)

const [payloadPath, outputPath] = process.argv.slice(2)
if (!payloadPath || !outputPath) throw new Error('usage: translate_android_resources.mjs payload.json output.json')

const payload = JSON.parse(await readFile(payloadPath, 'utf8'))
const client = new LlmClient({ timeoutMs: 900000, maxRetries: 3 })
const languages = [
  ['lg', 'Luganda'],
  ['zu', 'isiZulu'],
  ['sn', 'Shona'],
]
const results = {}

for (const [code, language] of languages) {
  const translated = {}
  for (const batch of payload.batches) {
    const response = await client.chat({
      model: 'gpt-5-mini',
      messages: [
        {
          role: 'system',
          content:
            `You are a professional Android UI translator. Translate English UI strings into natural ${language} for users in Africa. ` +
            'Return one translated value for every item, preserving the exact key, Android format placeholders (%s, %1$s, %d, %1$d), escaped apostrophes, line breaks, and technical/product names. ' +
            'Do not translate AmaBeat, YouTube, Spotify, GitHub, URLs, file extensions, or code-like identifiers. Keep translations concise and suitable for buttons and settings. ' +
            'For plural items, preserve the plural quantity key and translate only the text. Output JSON only.',
        },
        { role: 'user', content: JSON.stringify({ language, items: batch }) },
      ],
      response_format: {
        type: 'json_schema',
        json_schema: {
          name: 'android_translations',
          strict: true,
          schema: {
            type: 'object',
            properties: {
              translations: {
                type: 'array',
                items: {
                  type: 'object',
                  properties: {
                    key: { type: 'string' },
                    value: { type: 'string' },
                  },
                  required: ['key', 'value'],
                  additionalProperties: false,
                },
              },
            },
            required: ['translations'],
            additionalProperties: false,
          },
        },
      },
      max_completion_tokens: 90000,
    })
    const data = JSON.parse(messageText(response))
    for (const item of data.translations ?? []) translated[item.key] = item.value
    console.log(`${language}: ${Object.keys(translated).length}/${batch.length} translated`)
  }
  results[code] = translated
}

await writeFile(outputPath, JSON.stringify(results, null, 2) + '\n')
console.log(`Wrote ${outputPath}`)
