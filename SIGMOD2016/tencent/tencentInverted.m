set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x = [1, 2, 3, 4, 5];
y1 = [4699.944235	2005.765754	1190.830284	941.1294988	702.1582712];
y2 = [872.6229521	486.8822156	352.8290257	329.8513623	285.4495026];
y3 = [27418.01	9580.48	5384.46	3874.44	3258.36];
y4 = [2331.24	1590.24	1511.95	1327.04	1395.74];


p1= semilogy(x, y1, '-ks');
hold on;
p2 = semilogy(x, y2, '-kd');
hold on;
p3 = semilogy(x, y3, '-kv');
hold on;
p4 = semilogy(x, y4, '-k^');
hold on;

xlabel('k');
ylabel('time (ms)');

axis([0.5 5.5 0.0 100000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'4','5','6','7','8'});
%leg=legend('basic-g','basic-w','Inc-S','Inc-T','Dec', 1);
%set(leg,'edgecolor','white');

set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'FontSize', 12);
leg1 = legend([p1, p2],'Inc-S','Inc-T');
set(leg1,'edgecolor','white');

set(gca, 'LineWidth', 1.5);
ah=axes('position',get(gca,'position'),'visible','off');
set(gca, 'FontSize', 12);
leg2 = legend(ah,[p3, p4],'Inc-S*','Inc-T*');
set(leg2,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);


