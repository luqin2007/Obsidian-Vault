```typescript
export class TFile extends TAbstractFile {

/** @public 文件属性（创建/修改时间，文件大小） */
stat: FileStats;

/** @public 文件名（不包含扩展名） */
basename: string;

/** @public 扩展名 */
extension: string;
}
```