# 运行时管理 ACL

通过 `MutableAclService`，可以在运行时修改权限。

```java
@Autowired
private MutableAclService mutableAclService;

// 运行时向 admin 角色添加 read，administration，delete 权限
@Override
public void provideAccessToAdmin(int fixedDepositId) {
    addPermission(fixedDepositId, new PrincipalSid("admin"), BasePermission.READ);
    addPermission(fixedDepositId, new PrincipalSid("admin"), BasePermission.ADMINISTRATION);
    addPermission(fixedDepositId, new PrincipalSid("admin"), BasePermission.DELETE);
}

private void addPermission(int fixedDepositId, Sid recipient, Permission permission) {
    MutableAcl acl;
    ObjectIdentity old = new ObjectIdentityImpl(FixedDepositDetails.class, fixedDepositId);
  
    try {
        acl = (MutableAcl) mutableAclService.readAclById(old);
    } catch (NotFoundException e) {
        acl = mutableAclService.createAcl(old);
    }
  
    acl.insertAce(acl.getEntries().size(), permission, recipient, true);
    mutableAclService.updateAcl(acl);
}
```

如果要删除某个权限条目，只需要使用 `MutableAclService` 的删除方法即可。

```java
@Override
public void closeFixedDeposit(int fixedDepositId) {
    fixedDepositRepository.closeFixedDeposit(fixedDepositId);
    ObjectIdentity old = new ObjectIdentityImpl(FixedDepositDetails.class, fixedDepositId);
    mutableAclService.deleteAcl(old, false);
}
```
