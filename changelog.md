# 0.1.1+1.21.1

- Move `AbstractGenericInventoryBE`, `EntityIngredient` and `MFFluidTank` (renamed to `LazuliFluidTank`) from Minefactorial to Lazuli.
- Move `QuaternionHelpers` from Cygnus to Lazuli.
- Fix `RoundRobinInventory` not properly distributing items when `simulate` is true.
- Serialize `index` with `RoundRobinInventory` to maintain distribution across world exits.
- Add a codec for `AABB`s to `LazuliExtraCodecs`
- Move `icon.png` to `assets/lazuli/icon.png`
- Remove EMI from the build script so that it doesn't get transitively depended on when others use Lazuli.

# 0.1.0+1.21.1

- Gut foundations/APIs from Cygnus, Minefactorial, and Ala Sona to add them to Lazuli.
  - The only major changes I made were some cleanups and documenting pretty much everything.
