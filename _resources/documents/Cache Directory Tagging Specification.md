# Cache Directory Tagging Specification
## Version 0.6 ([Changes](https://bford.info/cachedir/#changes))
### Proposed by [Bryan Ford](https://bford.info/)
## Abstract

Many applications create and manage directories containing cached information about content stored elsewhere, such as cached Web content or thumbnail-size versions of images or movies. For speed and storage efficiency we would often like to avoid backing up, archiving, or otherwise unnecessarily copying such directories around, but it is a pain to identify and individually exclude each such directory during data transfer operations. I propose an extremely simple convention by which applications can reliably "tag" any cache directories they create, for easy identification by backup systems and other data management utilities. Data management utilities can then heed or ignore these tags as the user sees fit.
## The Problem

Many application programs create and manage cache directories of some kind: directories in which they store temporary information whose storage can benefit the application's performance, but which can easily be regenerated from other "primary" data sources if it is lost. For example, most web browsers have a cache directory in which they store downloaded Internet content ([Mozilla](http://www.mozilla.org/) typically uses a directory called Cache somewhere under `$(HOME)/.mozilla`). When backing up, synchronizing, or transferring a user's home directory from one machine to another, we would usually like to avoid saving or transferring cache directories at all, because:

- The contents of a cache directory generally has no long-term archival value, since it just mirrors source material stored elsewhere and often in a reduced or incomplete form - e.g., only the parts of a web page that the user has visited recently, or thumbnail-sized versions of pictures the user has browsed.
- Cache directories can grow to a fairly substantial size - especially cache directories for web browsers - potentially wasting considerable backup storage space and/or network bandwidth.
- The contents of a cache directory tends to change frequently even when the user is merely browsing or viewing files, which confuses incremental backup or synchronization software into seeing many frequent "false changes" in the directory tree and defeating the efficiency of the incremental algorithm.
- The files in a cache directory are often named very cryptically (e.g., based on a hash of the name or content of the original source material). Seeing thousands of such meaningless filenames scroll past during operations that traverse a user's home directory is annoying at best, and may confuse or frighten inexperienced users.

There are a great many applications that use cache directories, however. While some evolving standards attempt to standardize the locations of such directories, most applications create their own cache directories in unpredictable locations and manage them independently of other applications. It therefore becomes very tedious, even for experienced users who can easily recognize cache directories, to figure out and list all of the appropriate `--exclude` options for backups or other directory traversal operations. Some applications, such as recent versions of [KDE](http://www.kde.org/), keep their cache directories in `/tmp` or `/var/tmp`, which at least partly solves this problem by getting the cached content out of the user's home directory entirely. But there are good reasons an application or user might not want these caches in `/tmp` or `/var/tmp`: for example, those directories may be on a different partition with limited space, or files in them may be cleared out by the system too frequently (e.g., after a day, or on every reboot) to allow the cache to serve its intended purpose effectively.

The [Filesystem Hierarchy Standard](http://www.pathname.com/fhs/) specifies a system-wide `/var/cache` directory intended to hold such cached information, but this directory is not generally world-writable, and thus can only be used by applications pre-packaged with the system or installed by the system administrator. The [XDG Base Directory Specification](http://freedesktop.org/Standards/basedir-spec) instead recommends that applications create their caches in a location specified by a user environment variable, or in a standard subdirectory of the user's home directory if that environment variable doesn't exist. The XDG convention allows applications to operate without special permissions, but it does not help systemwide backup or data management utilities locate individual users' cache directories reliably, because each user might set the environment variable differently in his or her login profile.
## Proposed Solution

Given the present non-existence of any perfect agreement on where applications should store their cached information, I propose a very simple convention that will at least allow such information to be *identified* effectively. Regardless of where the application decides to (or is configured to) place its cache directory, it should place within this directory a file named:

```
CACHEDIR.TAG
```

This file must be an ordinary file, not for example a symbolic link. Additionally, *the first 43 octets* of this file must consist of the following ASCII header string:

```
Signature: 8a477f597d28d172789f06886806bc55
```

Case is important in the header signature, there can be no whitespace or other characters in the file before the 'S', and there is exactly one space character (ASCII code 32 decimal) after the colon. The header string does not have to be followed by an LF or CR/LF in the file in order for the file to be recognized as a valid cache directory tag. The hex value in the signature happens to be the MD5 hash of the string "`.IsCacheDirectory`". This signature header is required to avoid the chance of an unrelated file named `CACHEDIR.TAG` being mistakenly interpreted as a cache directory tag by data management utilities, and (for example) causing valuable data not to be backed up.

The content of the remainder of the tag file is currently unspecified, except that it should be a plain text file in UTF-8 encoding, and any line beginning with a hash character ('#') should be treated as a comment and ignored by any software that reads the file.

We will henceforth refer to a file named as specified above, and having the required signature at the beginning of its content, as a *cache directory tag*.

For the benefit of anyone who happens to find and look at a cache directory tag directly, it is recommended that applications include in the file a comment referring back to this specification. For example:

```
Signature: 8a477f597d28d172789f06886806bc55
# This file is a cache directory tag created by (application name).
# For information about cache directory tags, see:
#	http://www.brynosaurus.com/cachedir/
```

The official "home" URL of this specification may change from the above at some point if/when this proposal becomes more of a formal standard, but in that case an appropriate forwarding link will be left in the old location.
## Application Semantics

The presence of a cache directory tag within a given directory indicates that the entire contents of that directory, including any and all subdirectories underneath it, consists of cached information that can be re-generated if necessary from appropriate source material located elsewhere. An application that creates and/or uses a cache directory should write a single cache directory tag into the topmost directory whose contents represent cached information. For example, if Mozilla creates a directory named `$(HOME)/.mozilla/default/dg8t1ih0.slt/Cache` to hold cached content downloaded from the Internet, it would then create within it a cache directory tag named: `$(HOME)/.mozilla/default/dg8t1ih0.slt/Cache/CACHEDIR.TAG`. Only one cache directory tag is required to tag an entire subdirectory tree of cached content. The application should also regenerate the cache directory tag if it disappears: e.g., if the entire contents of the cache directory is deleted without the directory itself being deleted.

Data backup, transfer, or synchronization software, if configured to do so, may interpret the presence of a cache directory tag in a directory as a command to exclude that directory automatically (including any subdirectories) from the backup or transfer operation. I leave it to the designers of such applications to decide whether this "auto-exclude" behavior should be the default or only adopted when specifically requested by the user. A bit of conservatism is probably warranted in the short term, but automatically excluding cache directories by default could improve efficiency and ease-of-use in the longer term.

For maximum robustness against accidental data loss, any tool that has built-in support for cache directory tags is *strongly encouraged* to verify that any file it finds named `CACHEDIR.TAG` is a regular file and actually contains the 43-byte header specified above, before concluding that the file is indeed a cache directory tag.

User-friendly file managers and other directory hierarchy browsing software may also want to notice the presence of a cache directory tag in a directory, and inform the user that the contents of the directory is automatically managed and is not likely to make much sense if "browsed" directly.

System administration software such as `du` might provide a way to locate, total, and report the space usage of all the cache directories on a partition, helping the administrator to determine how much "soft" space is in use on a file system that could be easily freed up if necessary. Similarly, a utility might allow the administrator to clean out all the cache directories on the system, or to delete files in cache directories beyond a certain "age."

System software *should not*, however, just periodically go through and unilaterally delete "old" files in arbitrary cache directories on a purely automatic basis. Different applications need to be able to manage their caches differently; cached data that is considered "old" to one application or to the system might not be considered "old" to another (e.g., if certain data takes a long time to regenerate). In the future this standard might be extended to allow applications to write additional meta-data in their cache directory tags, providing system utilities with the additional information they would need to manage application caches intelligently on a global basis. For now, however, the primary purpose for introducing cache directory tags is merely to make it easy to avoid backing up or copying cached data unnecessarily from one machine to another.
## Security Considerations

"Blind" use of cache directory tags in automatic system backups could potentially increase the damage that intruders or malware could cause to a system. A user or system administrator might be substantially less likely to notice the malicious insertion of a `CACHDIR.TAG` into an important directory than the outright deletion of that directory, for example, causing the contents of that directory to be omitted from regular backups. To mitigate this risk, backup software should at least inform the user which directories are being omitted due to the presence of cache directory tags. Automatic incremental backup software might maintain a list of "approved" cache directories, and whenever new cache directory tags appear, only heed them after being approved by the system administrator. In short, to maintain robustness of backups in the face of security compromises, cache directory tags should only be treated as hints, never as "the final word" on what should and should not be backed up.
## Alternative Approaches

This section discusses a few of the alternatives that were considered in developing this proposal, and the rationale for designing the proposal as specified. It is formatted as a mini-FAQ addressing the most common questions.

- **Why not just standardize the location where applications keep their cache directories?** First, there are already multiple conflicting standards: e.g., the [Filesystem Hierarchy Standard](http://www.pathname.com/fhs/) specifies `/var/cache`, [KDE](http://www.kde.org/) currently uses `/var/tmp`, and the [XDG Base Directory Specification](http://freedesktop.org/Standards/basedir-spec) specifies the use of a user-customizable environment variable, falling back on `$(HOME)/.cache` in the user's home directory. Even if one such standard eventually "wins", there is inevitable tension between standardizing the placement of application caches and allowing users to customize it. The XDG specification clearly recognizes this tension and accounts for it, but its very flexibility makes it difficult or impossible for systemwide utilities to locate each user's cache directory reliably, even if all applications were to conform to the XDG standard.

- **Why not just use the existing "no-backup" or "no-dump" extended file attributes that some operating systems support?** Because only some operating systems (and generally only certain specific file systems in them) support those attributes. Any proposed convention for tagging cache directories that depends on non-portable OS features would inherently have limited applicability and would not likely be widely adopted among portable applications. Applications are free to support system-specific extensions when available, but for maximum usefulness we really need one extremely simple, portable convention that caching applications can adopt with almost no effort, and that is the purpose of this proposal.

- **Why shouldn't all web browsers just use the same cache?** Sharing a single Internet content cache among multiple web browsers or other web applications, all of which might be running concurrently, is a very useful and important goal - but it presents considerable technical challenges that are still being worked out. Even when such sharing is achieved for web content caches, however, many other applications maintain other types of on-disk caches in which they store entirely different kinds of data, such as intermediate results for long-running computations or swap space for large data sets. It is unreasonable to expect applications to share caches when those caches serve entirely different purposes and inherently must be managed differently. But it would still be useful, and it is much more realistic, to expect applications at least to make their caches easily identifiable as such by adopting a simple convention such as the one proposed here.

- **Why does the proposal only support cache directories - what if an application only has a single file that it uses as a cache?** Because the tagging scheme is much simpler and easier to take advantage of this way, and imposes no great burden on caching applications. If you're not paranoid about checking cache directory tag file headers, then most existing, unmodified backup/archive applications can immediately take advantage of this tagging convention via some pretty trivial shell-fu. For example:

```
find . -name CACHEDIR.TAG | sed -e 's/[/]CACHEDIR.TAG$//g' >/tmp/excludes
tar cvf /tmp/backup.tar --exclude-from=/tmp/excludes .
```

Trying to support the tagging of individual files within a directory would probably mean at least having to parse text files and scan them for file names, making the convention much more complex and potentially delicate at the outset. And when it comes to standards that affect what data gets backed up and what doesn't, we obviously would like them to be as simple and robust as possible.

For those existing caching applications that keep all their cached data in a single file, it should not be inordinately difficult for them just to move that cache file down one level into its own separate subdirectory, and write a cache directory tag into that new subdirectory alongside the cache file. Such a slight directory structure change should not cause any backward compatibility problems, since by definition any cache file in the old location is regenerable and can just be deleted or moved. And users won't care because they aren't normally expected to see directly or understand the application's cache files or directories anyway.
## Conclusion

By adopting the convention described above of writing cache directory tags where appropriate, applications that create and manage cache directories will make it much easier for users and system administrators to identify and manage "soft" storage areas whose contents are useful for performance but do not need to be backed up or synchronized across machines. This proposal deliberately attempts to adopt the simplest reasonable and portable solution, in the interest of maximizing robustness while minimizing cost of adoption, and hence hopefully maximizing its chance of eventual widespread adoption. The proposal nevertheless leaves room for future enhancements, in which applications might include within their cache directory tags additional useful information about the semantics and management of their cache directories.
## Changes

- **Version 0.6, November 4, 2019**: Web page consolidation and restructuring; no substantive changes.

- **Version 0.5, October 27, 2004**: Added Security Considerations section in response to a potential vulnerability pointed out by Gregory P. Smith.

- **Version 0.4, August 10, 2004**: Changed cache tag file name from "`.IsCacheDirectory`" to "`CACHEDIR.TAG`", to be compatible with old 14-character POSIX file systems and 8.3-character MS-DOS file systems.

- **Version 0.3, July 29, 2004**: Recommend that apps include a comment in their tags referring back to this spec. Recommend that backup/archival apps verify tag headers. Discuss tag filename length issue.

- **Version 0.2, July 23, 2004**: Fleshed out Alternative Approaches section based on various feedback. Removed reference to thumbnail managing standard since the thumbnails directory doesn't quite have pure cache semantics.

- **Version 0.1, July 19, 2004**: First public release.
## Application Support Status

| Caching Applications | Archive/Backup Tools |
| --- | --- |
| The following applications are known to create and maintain caches of some kind in the user's home directory.<br>Applications that currently support this specification:<br><br>[BZFlag multi-player 3D game](http://www.bzflag.org/) as of 2004-07-24. ([Feature Request](https://sourceforge.net/tracker/?func=detail&atid=353248&aid=997021&group_id=3248)))<br>[libdvdcss](http://developers.videolan.org/libdvdcss/) as of 2004-08-12. ([Feature Request](http://bugzilla.videolan.org/cgi-bin/bugzilla/show_bug.cgi?id=1863))<br>VLC as of 2004-10-27.<br>WWWOFFLE as of 2006-02-05.<br>Positive responses received from developers of:<br><br>[Mozilla web browser](http://www.mozilla.org/) ([Feature Request](http://bugzilla.mozilla.org/show_bug.cgi?id=252179))<br>[Galeon web browser](http://galeon.sourceforge.net/)<br>[OpenOffice.org](http://www.openoffice.org/) ([Feature Request](http://www.openoffice.org/issues/show_bug.cgi?id=32052))<br>[ccache](http://ccache.samba.org/)<br><br>Discussion initiated with developers of:<br><br>[The GIMP](http://www.gimp.org/)<br>[aRts/MCOP](http://www.arts-project.org/) | The following archival tools support this tagging convention:<br><br>[tar archiving/backup tool](http://www.gnu.org/software/tar/tar.html):use the new `--exclude-caches` option.([patch to tar-1.14](https://bford.info/cachedir/tar-1.14.diff)) ([patch to tar-CVS](https://bford.info/cachedir/tar-cvs.diff))<br><br>Other archival tools that are not (yet) directly sensitive to cache tags, but can be made so through the use of appropriate options or shell magic:<br><br>[cpio archiving/backup tool](http://www.gnu.org/software/cpio/cpio.html)<br>[rsync incremental transfer/synchronization tool](http://samba.anu.edu.au/rsync/)<br>[DAR disk archiver](https://sourceforge.net/projects/dar/) ([Feature Request](https://sourceforge.net/tracker/?func=detail&atid=511615&aid=994532&group_id=65612))<br> |
Please let me know of other applications you know of that should be on this list!
## Related Links

- [Filesystem Hierarchy Standard](http://www.pathname.com/fhs/)
- [XDG Base Directory Specification](http://freedesktop.org/Standards/basedir-spec)
- [Thumbnail Managing Standard](http://triq.net/~jens/thumbnail-spec/)

# 参考

```cardlink
url: https://bford.info/cachedir/
title: "Cache Directory Tagging Specification – Bryan Ford's Home Page"
host: bford.info
```
