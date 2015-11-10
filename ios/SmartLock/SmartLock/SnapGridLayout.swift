//
//  SnapGridLayout.swift
//  SmartLock
//
//  Created by Sam Davies on 02/11/2015.
//  Copyright © 2015 Sam Davies. All rights reserved.
//

import UIKit

class SnapGridLayout: UICollectionViewFlowLayout {
    
    @IBOutlet var grid: UICollectionView!
    
    override func targetContentOffsetForProposedContentOffset(proposedContentOffset: CGPoint, withScrollingVelocity velocity: CGPoint) -> CGPoint {
        
        var offsetAdjustment: CGFloat = CGFloat.max
        let horizontalCenter: CGFloat = proposedContentOffset.x + self.grid.bounds.size.width / 2.0
        let targetRect: CGRect = CGRectMake(proposedContentOffset.x, 0.0, self.grid.bounds.size.width, self.grid.bounds.size.height)
        
        let attributes = super.layoutAttributesForElementsInRect(targetRect)!
        for a: UICollectionViewLayoutAttributes in attributes {
            let itemHorizontalCenter: CGFloat = a.center.x;
            if (abs(itemHorizontalCenter - horizontalCenter) < abs(offsetAdjustment)) {
                offsetAdjustment = itemHorizontalCenter - horizontalCenter
            }
        }
        
        return CGPointMake(proposedContentOffset.x + offsetAdjustment, proposedContentOffset.y);
    }
    
}
